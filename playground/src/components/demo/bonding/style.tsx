import styled from 'styled-components';
import {View} from "react-native";

export const Container = styled(View)`
  display: flex;
  flex-direction: column;
  height: 120px;
`;

export const ListItem = styled(View)`
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  border-style: solid;
  border-width: 4px;
  border-right-width: 0;
  border-top-width: 0;
  border-bottom-width: 0;
  border-color: #e6e6e6;
`;

export const ListItemInside = styled(View)`
  width: 100%;
  min-height: 16px;
  padding-bottom: 2px;
  padding-left: 6px;
  display: flex;
  flex-direction: row;
`;
