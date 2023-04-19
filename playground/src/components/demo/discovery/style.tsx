import styled from 'styled-components';
import {View} from "react-native";

export const Container = styled(View)`
  display: flex;
  flex-direction: column;
  height: 200px;
`;

export const ListItem = styled(View)`
  width: 100%;
  min-height: 16px;
  padding-bottom: 2px;
  padding-left: 6px;
  display: flex;
  flex-direction: row;
`;


export const ButtonGroup = styled(View)`
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  justify-content: flex-start;
  margin-top: 10px;
`
